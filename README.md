# PRCO304

Pulls trade data from GDAX API, creates average price and then decides
whether to trade holdings into another currency.

Known Issues:

PriceCollector
Gaps not clearing properly (database free mode)
Need to switch off historic collection after lap 2

Currency
Issue with findGaps (first loop) - seems to occur with 0 gaps