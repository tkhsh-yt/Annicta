package tkhshyt.annicta.auth

import tkhshyt.annict.json.AccessToken

interface AuthNavigator {

    fun onAuthorize(accessToken: AccessToken)

    fun onFailToAuthorize()
}