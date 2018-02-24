package tkhshyt.annicta.event

import tkhshyt.annict.Season
import tkhshyt.annicta.SeasonSpinner

/**
 * 表示する作品のシーズンが選択されたときのイベント．
 */
class SeasonSpinnerSelectedEvent(val season: SeasonSpinner)

/**
 * 表示する作品の期間が選択されたときのイベント．
 *
 * @param season 選択された期間
 */
class SeasonSelectedEvent(val season: Season)