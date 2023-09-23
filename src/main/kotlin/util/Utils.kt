package dev.thynanami.nextstop.backend.util

import com.password4j.Argon2Function
import com.password4j.Password
import com.password4j.types.Argon2
import dev.thynanami.nextstop.backend.dao.dao

val argon2: Argon2Function = Argon2Function.getInstance(47104, 1, 1,128,Argon2.ID)

fun generateToken():String {
    val charset ="ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..256).map { charset.random() }.joinToString("")
}

suspend fun passwordIsVerified(username:String, password:String):Boolean {
    val account = dao.queryAccount(username = username)
    return Password.check(password, account!!.password).addPepper("YuanShenQiDong").with(argon2)
}

fun hashPassword(password: String): String {
    return Password.hash(password).addRandomSalt(32).addPepper("YuanShenQiDong").with(argon2).result
}