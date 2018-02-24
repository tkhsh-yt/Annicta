package tkhshyt.annicta.event

/**
 * Annict との連携を認証するページを開くイベント．
 */
class OpenAnnictEvent

/**
 * Annict との連携の認証に失敗したときのイベント．
 */
class FailToAuthorizeEvent(throwable: Throwable)