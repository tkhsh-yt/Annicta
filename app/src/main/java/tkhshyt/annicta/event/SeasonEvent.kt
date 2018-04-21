package tkhshyt.annicta.event

import tkhshyt.annict.Season
import tkhshyt.annicta.main.works.SeasonSelectSpinner

class SeasonSelectedEvent(val season: Season)

class SeasonSpinnerSelectedEvent(val selected: SeasonSelectSpinner)