package com.go.service

import com.go.controller.LoginParam

interface IUserService {
    fun login(param: LoginParam): String
}