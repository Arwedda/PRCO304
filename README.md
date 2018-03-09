# PRCO304

Pulls trade data from GDAX API, creates average price and then decides
whether to trade holdings into another currency.

Known Issues:

PriceCollector
Found condition where a single currency was being ignored - it had its gaps cleared erroneously
Gaps not clearing properly (database free mode)

Currency
Issue with findGaps (first loop) - seems to occur with 0 gaps