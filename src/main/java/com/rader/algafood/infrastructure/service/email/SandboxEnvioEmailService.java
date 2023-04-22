package com.rader.algafood.infrastructure.service.email;

import com.rader.algafood.core.email.EmailProperties;
import com.rader.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SandboxEnvioEmailService extends SmtpEnvioEmailService {

    @Autowired
    private EmailProperties emailProperties;

    @Override
    protected MimeMessage criarMimeMessage(EnvioEmailService.Mensagem mensagem) throws MessagingException {
        MimeMessage mimeMessage = super.criarMimeMessage(mensagem);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setTo(emailProperties.getSandbox().getDestinatario());

        return mimeMessage;
    }
}