package tkhshyt.annict.json

import java.io.Serializable

/**
 * TokenInfo に含まれる application を表すクラス．
 *
 * @property uid ユーザ識別子
 */
data class Application(
        val uid: String
) : Serializable