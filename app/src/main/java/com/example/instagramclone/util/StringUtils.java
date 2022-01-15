package com.example.instagramclone.util;

import java.io.Serializable;

public class StringUtils implements Serializable {
    public static final String emptyFields = "Existem um ou mais campos vazios";
    public static final String registeredSuccessfully = "cadastrado com sucesso";
    public static final String weakPassword = "senha muito fraca";
    public static final String invalidEmail = "Este e-mail não é válido";
    public static final String emailRegistered = "já existe uma conta com esse e-mail";
    public static final String errorToRegister = "erro ao cadastrar usuario";
    public static final String unregisteredUser = "usuário não cadastrado";
    public static final String incorrectEmailOrPassword = "E-mail ou senha incorretos";
    public static final String errorLoggingInUser = "Erro ao logar usuario";
    public static final String loginSuccess = "Sucesso ao logar";

    /* folders in firebase */
    public static final String users = "users";

    /* keys intent */
    public static final String friendProfile = "friendProfile";
}
