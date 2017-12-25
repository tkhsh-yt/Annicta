package tkhshyt.annict

/**
 * HTTPステータスコード
 */
object HttpStatusCode {

    /** リクエストが成功し，同時に処理が正常に完了したとき． */
    val Ok = 200

    /** リクエストが成功し，新しいリソースが作られたとき． */
    val Created = 201

    /** リクエストが成功したが，処理は非同期で実行されるとき． */
    val Accepted = 202

    /** リクエストが成功し，レスポンスボディが存在しないとき．
     * DELETE メソッドでリソースが削除されたときに使用する． */
    val NoContent = 204

    /** リクエストの内容が正しくないとき． */
    val BadRequest = 400

    /** リソースへのアクセスに認証が必要なとき．
     * リクエストを送ったクライアントが特定できないときに使用する． */
    val Unauthorized = 401

    /** リソースへのアクセスが禁止されているとき．
     * リクエストを送ったクライアントは特定したものの，指定されたリソースへのアクセスを禁止しているときに使用する． */
    val Forbidden = 403

    /** 指定されたリソースが存在しないとき． */
    val NotFound = 404

    /** Annictのサーバ内部に問題があり正常にレスポンスが返せないとき． */
    val InternalServerError = 500

    /** Annictのサーバが一時的に停止しているとき． */
    val ServiceUnavaiable = 503
}
