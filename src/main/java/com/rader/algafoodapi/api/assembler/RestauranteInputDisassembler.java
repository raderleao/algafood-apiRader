package com.rader.algafoodapi.api.assembler;

import com.rader.algafoodapi.api.model.CozinhaModel;
import com.rader.algafoodapi.api.model.RestauranteModel;
import com.rader.algafoodapi.api.model.input.RestauranteInput;
import com.rader.algafoodapi.domain.model.Cidade;
import com.rader.algafoodapi.domain.model.Cozinha;
import com.rader.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
       return modelMapper.map(restauranteInput, Restaurante.class);
    }

    public void copyToDomainObject (RestauranteInput restauranteInputq, Restaurante restaurante) {
        //Para evitar org.hibernate.HibernateException: identifier of an instance of
        // com.rader.algafoodapi.domain.model.Cozinha was altered from 2 to 1
        restaurante.setCozinha(new Cozinha());

        if (restaurante.getEndereco() != null){
            restaurante.getEndereco().setCidade(new Cidade());
        }

        modelMapper.map(restauranteInputq, restaurante);


    }
}
