package com.aries.servlet.jwt

import com.auth0.jwt.interfaces.RSAKeyProvider
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*


class MyRSAKeyProvider: RSAKeyProvider{

    val publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoolXoYFhlYKaSWizDPGpWjrj/" +
            "FHpVtMYndyJPEhm7vPStQsR6VEgritIrKF2b038ApSIqfsH4gLZQKrURK3M32K9+" +
            "qmY30uHriUvfAxLWQSZR3GRX4ZwQpN2V/1ifNHXRxjg8Q9weRrqjaD5IKgo/4GLa" +
            "hIhO0ltYC1i+qa5EXwIDAQAB"


    val privateKeyString = "MIICxjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQI9BeDg7qQDNoCAggA" +
            "MBQGCCqGSIb3DQMHBAhABTK9Aog+CwSCAoBtZ1kFXmYINlUO+Pzx1Dh+wco0khfA" +
            "PmriShd1OMzEDtcyyg8FzyA6Y62Ilzee+KA3iroYHnP0gn1f6+Y1GF/Bnswwn6cZ" +
            "5CIZz0XCnhutUg5Jkf2v+yhCacj2X6A8aANrHBgQAtop0UqGq6+LxkKAaxN6sRcY" +
            "zk24s1AyZq4MMuASeH+YZPiyi57IH7Eeb2rukAAO73eC1wc3uize5R54d0tMgaW5" +
            "MYcz+QT3jd7dlwR0F4b4sU5YRP2HU8gBCN1Nh52mBfU9FFbUtQ8UEs4UDX2xxRTJ" +
            "Ne++0byv+yWB/XmwxDvugqs2JnroePEz4eTquWLW5EvCiIItOdSPlVV4rWBYOgK7" +
            "2XyX7SQF3UdySl6we9/wpOAk/fTLAtb+KpOx3GZv10wvWi7VSqS/udAVIqDOu8KJ" +
            "T1dwTcwQNO01ZdbF9sVWDlmyp08PgTGplYpby6bV1o6rmeAdxhz6gEXa/x2KNqji" +
            "tLKW16hzU+j5TPAv1v0t9VHbMwswTTLx6TiOom9CtKHNYzutokcqilks2vGU9rWz" +
            "0C0wl8dQ0+2nIqi0Bgp9NOCN6gVQziZHUkGefoCWMgrGxSR6UWnL0POpl1jK2a7k" +
            "D2WBBLUJ8BSdLgFhLbeDTbPU8LWe4xLNqJUITprSQIlfESFhXFlf/uF5Xb1aPXHL" +
            "Ll1KfH3C24VYr9+bVSE++bfTTvINhTDs4R1rXN21jclx6xpaeb5/JFW3tH8IhjnD" +
            "hQMjrsxVmLvTYWMuwpsN6ZpvpXWg3mI2i2jhHNSuY4khnUcpSTRJCRk39bSbZYeI" +
            "YYvZPuCyp0HCo1BeUKi5Z2U71Vclfzt52WEajGz4CtDcEGu57lYSxeBl"


    val RSA_ALGORITHM = "RSA"
    val _publicKey: RSAPublicKey
    val _privateKey: RSAPrivateKey

    init {

        _privateKey = generatePrivateKey(privateKeyString)
        _publicKey =generatePublicKey(publicKeyString)

    }

    override fun getPrivateKeyId(): String? {
        return null
    }

    override fun getPrivateKey(): RSAPrivateKey {
        return _privateKey
    }

    override fun getPublicKeyById(keyId: String): RSAPublicKey {
        return _publicKey
    }

    private fun generatePublicKey(publicKey:String):RSAPublicKey{
        val keyBytes = Base64.getDecoder().decode(publicKey.toByteArray())
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    private fun generatePrivateKey(keyString:String):RSAPrivateKey{
        val keyBytes = Base64.getDecoder().decode(keyString.toByteArray())
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

}