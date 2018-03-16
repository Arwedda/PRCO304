# PRCO304

Pulls trade data from GDAX API, creates average price and then decides
whether to trade holdings into another currency.

Known Issues:

PriceCollector
BTC fails to collect sometimes - other currencies working fine and restart fixes this
Need to switch off historic collection after lap 2
[Database mode] PUT stops GUI visibility

Currency
Issue with findGaps (first loop) - seems to occur with 0 gaps