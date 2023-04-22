package com.rader.algafood.api.assembler;

import com.rader.algafood.api.model.input.RestauranteInput;
import com.rader.algafood.domain.model.Cidade;
import com.rader.algafood.domain.model.Cozinha;
import com.rader.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
