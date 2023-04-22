#! /bin/sh -e

SERVICE_KEY=
TMP_DIR=.
DOWNLOAD_URL="https://files.appoptics.com/java"
VERSION=latest

test_write_access() {
    set +e
    WRITE_LOCATION=$1
    if [ -z "$WRITE_LOCATION" ]; then
        echo "Empty location given."
        exit 1
    fi
    if [ -d $WRITE_LOCATION ]; then
        touch $WRITE_LOCATION/tmp_write 1>/dev/null 2>&1
        if [ $? != 0 ]; then
            echo "No write access to $WRITE_LOCATION. Run script as root."
            exit 1
        fi
        rm -f $WRITE_LOCATION/tmp_write
    elif [ -f $WRITE_LOCATION ]; then
        touch $WRITE_LOCATION 1>/dev/null 2>&1
        if [ $? != 0 ]; then
            echo "No write access to $WRITE_LOCATION. Run script as root."
            exit 1
        fi
    else
        echo "$WRITE_LOCATION not found."
        exit 1
    fi
    set -e
}

check_sha256sum_installed() {
    # check if sha256sum is installed
    if [ -z "$(command -v sha256sum)" ]; then
        echo "Need sha256sum to verify checksums."
        exit 1
    fi
}

##############################################################################

print_usage_and_exit() {
    echo "Usage: $0 --service-key=<KEY> [--version=<VERSION>] [--url=<URL>]"
    echo "--service-key: unique service identifier"
    echo "--version: version to install (optional, default: latest)"
    echo "--url: URL to get the Java agent from (optional, default: https://files.appoptics.com/java)"
    exit 1
}

for i in "$@"; do
    if [ -n "$i" ]; then
        case $i in
            --service-key=*)
            SERVICE_KEY=$(echo $i | awk -F= {'print $2'})
            ;;
            --version=*)
            VERSION=$(echo $i | awk -F= {'print $2'})
            ;;
            --url=*)
            DOWNLOAD_URL=$(echo $i | awk -F= {'print $2'})
            ;;
            *)
            echo "Unknown or incomplete option: $i"
            print_usage_and_exit
            ;;
        esac
    fi
done

if [ -z "$SERVICE_KEY" ]; then
    echo "Please pass in a service key via the --service-key option."
    print_usage_and_exit
fi
if [ -z "$VERSION" ]; then
    echo "No version given with --version."
    print_usage_and_exit
fi
if [ -z "$DOWNLOAD_URL" ]; then
    echo "No URL given with --url."
    print_usage_and_exit
fi

test_write_access $TMP_DIR

##############################################################################

JAR_FILE=appoptics-agent.jar
API_FILE=appoptics-sdk.jar
JSON_FILE=javaagent.json

echo "Downloading from $DOWNLOAD_URL/$VERSION ..."
# check if wget or curl is installed
if [ -z "$(command -v curl)" ]; then
    if [ -z "$(command -v wget)" ]; then
        FETCH_CMD=
    elif [ -n "$(wget 2>&1 | grep -i busybox)" ]; then
        echo "Cannot use BusyBox version of wget. Please install full version of wget."
        exit 1
    else
        FETCH_CMD="wget --timeout=10 --tries=1 -O"
    fi
else
    FETCH_CMD="curl -f -m 10 --retry 1 -o"
fi
if [ -z "$FETCH_CMD" ]; then
    echo "Need either wget or curl installed."
    exit 1
fi

# ensure tmp dir exists
mkdir -p $TMP_DIR

# download files
$FETCH_CMD $TMP_DIR/$JAR_FILE $DOWNLOAD_URL/$VERSION/$JAR_FILE
$FETCH_CMD $TMP_DIR/$JAR_FILE.sha256 $DOWNLOAD_URL/$VERSION/$JAR_FILE.sha256
$FETCH_CMD $TMP_DIR/$API_FILE $DOWNLOAD_URL/$VERSION/$API_FILE
$FETCH_CMD $TMP_DIR/$API_FILE.sha256 $DOWNLOAD_URL/$VERSION/$API_FILE.sha256
$FETCH_CMD $TMP_DIR/$JSON_FILE.new $DOWNLOAD_URL/$VERSION/$JSON_FILE
$FETCH_CMD $TMP_DIR/VERSION $DOWNLOAD_URL/$VERSION/VERSION

# verify checksum
if [ -f $TMP_DIR/$JAR_FILE ] || [ -f $TMP_DIR/$JAR_FILE.sha256 ]; then
    check_sha256sum_installed
    SHA256=$(sha256sum $TMP_DIR/$JAR_FILE | awk {'print $1'})
    if [ "$(cat $TMP_DIR/$JAR_FILE.sha256)" != "$SHA256" ]; then
        echo "SHA256 checksum of file $TMP_DIR/$JAR_FILE doesn't match:"
        echo "Expected: $(cat $TMP_DIR/$JAR_FILE.sha256)"
        echo "Actual:   $SHA256"
        exit 1
    fi
else
    echo "$TMP_DIR/$JAR_FILE and/or $TMP_DIR/$JAR_FILE.sha256 not found."
    exit 1
fi

if [ -f $TMP_DIR/$API_FILE ] || [ -f $TMP_DIR/$API_FILE.sha256 ]; then
    check_sha256sum_installed
    SHA256=$(sha256sum $TMP_DIR/$API_FILE | awk {'print $1'})
    if [ "$(cat $TMP_DIR/$API_FILE.sha256)" != "$SHA256" ]; then
        echo "SHA256 checksum of file $TMP_DIR/$API_FILE doesn't match:"
        echo "Expected: $(cat $TMP_DIR/$API_FILE.sha256)"
        echo "Actual:   $SHA256"
        exit 1
    fi
else
    echo "$TMP_DIR/$API_FILE and/or $TMP_DIR/$API_FILE.sha256 not found."
    exit 1
fi

# delete checksum file
rm -f $TMP_DIR/$JAR_FILE.sha256
rm -f $TMP_DIR/$API_FILE.sha256

##############################################################################

# add service key to json file
if [ ! -f $TMP_DIR/$JSON_FILE.new ]; then
    echo "$TMP_DIR/$JSON_FILE.new not found"
    exit 1
else
    # write service key to json file
    sed -i 's/\(^[ \t]*"agent\.serviceKey"[ \t]*:\).*,/\1"'$SERVICE_KEY'",/g' $TMP_DIR/$JSON_FILE.new
fi

# save checksum of default json (needed for being able to tell if json file has been changed)
# this needs to happend AFTER the service key has been written to the file
check_sha256sum_installed
sha256sum $TMP_DIR/$JSON_FILE.new | awk {'print $1'} > $TMP_DIR/$JSON_FILE.new.sha256

# check if json file needs to be updated
JSON_UPDATE=no
if [ -f "$TMP_DIR/$JSON_FILE" ] && [ -f "$TMP_DIR/$JSON_FILE.sha256" ]; then
    SHA256_1=$(cat $TMP_DIR/$JSON_FILE.sha256)
    SHA256_2=$(cat $TMP_DIR/$JSON_FILE.new.sha256)
    if [ "$SHA256_1" != "$SHA256_2" ]; then
        JSON_UPDATE=yes
    fi
else
    JSON_UPDATE=yes
fi

# check if json file has been changed
JSON_CHANGED=no
if [ -f "$TMP_DIR/$JSON_FILE" ]; then
    if [ -f "$TMP_DIR/$JSON_FILE.sha256" ]; then
        check_sha256sum_installed
        SHA256_1=$(sha256sum $TMP_DIR/$JSON_FILE | awk {'print $1'})
        SHA256_2=$(cat $TMP_DIR/$JSON_FILE.sha256)
        if [ "$SHA256_1" != "$SHA256_2" ]; then
            JSON_CHANGED=yes
        fi
    else
        JSON_CHANGED=yes
    fi
fi

REAL_VERSION=$(cat $TMP_DIR/VERSION)
rm -f $TMP_DIR/VERSION

EXIT_CODE=0
if [ "$JSON_UPDATE" = "yes" ]; then
    if [ "$JSON_CHANGED" = "yes" ]; then
        echo "$TMP_DIR/$JSON_FILE has been modified (or checksum file not found) so we won't replace it."
        echo "Instead the new json file will be stored as $TMP_DIR/$JSON_FILE.$REAL_VERSION."
        echo "Please transfer all custom changes to $TMP_DIR/$JSON_FILE.$REAL_VERSION and rename to $TMP_DIR/$JSON_FILE."
        mv $TMP_DIR/$JSON_FILE.new $TMP_DIR/$JSON_FILE.$REAL_VERSION
        
        EXIT_CODE=1
    else
        mv $TMP_DIR/$JSON_FILE.new $TMP_DIR/$JSON_FILE
    fi
    
    mv $TMP_DIR/$JSON_FILE.new.sha256 $TMP_DIR/$JSON_FILE.sha256
else
    rm -f $TMP_DIR/$JSON_FILE.new $TMP_DIR/$JSON_FILE.new.sha256
fi

if [ "$EXIT_CODE" != "0" ]; then
    echo "Done installing the Java agent. Could not update the json file. See logs above."
else
    echo "Done installing the Java agent."
fi
exit $EXIT_CODE