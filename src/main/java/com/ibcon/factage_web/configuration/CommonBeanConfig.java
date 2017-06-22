package com.ibcon.factage_web.configuration;

import com.ibcon.factage_web.services.crud.role.RoleService;
import com.ibcon.factage_web.services.crud.role.RoleServiceImpl;
import com.ibcon.factage_web.services.crud.user.UserServiceCrud;
import com.ibcon.factage_web.services.crud.user.UserServiceCrudImp;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeanConfig {

    @Bean
    public StrongPasswordEncryptor strongEncryptor(){
        return new StrongPasswordEncryptor();
    }

    @Bean
    public RoleService roleService() {
        return new RoleServiceImpl();
    }

    @Bean
    public UserServiceCrud userServiceCrud() {
        return new UserServiceCrudImp();
    }
}
