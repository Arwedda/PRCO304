using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using CryptocurrencyPredictorAPI.Models;

namespace CryptocurrencyPredictorAPI.Controllers
{
    public class ExchangeRateController : ApiController
    {
        private ExchangeRates db = new ExchangeRates();

        // GET: api/ExchangeRate
        public IQueryable<PRCO304_EXCHANGERATE> GetPRCO304_EXCHANGERATE()
        {
            return db.PRCO304_EXCHANGERATE;
        }

        // GET: api/ExchangeRate/{currency_id}/{timestamp}
        [Route("api/ExchangeRate/{currency_id}/{timestamp}")]
        [ResponseType(typeof(PRCO304_EXCHANGERATE))]
        public async Task<IHttpActionResult> GetPRCO304_EXCHANGERATE(string currency_id, string timestamp)
        {
            DateTime timestamp_dt = Convert.ToDateTime(timestamp);
            PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE = await db.PRCO304_EXCHANGERATE.FindAsync(currency_id, timestamp_dt);
            if (pRCO304_EXCHANGERATE == null)
            {
                return NotFound();
            }

            return Ok(pRCO304_EXCHANGERATE);
        }

        // PUT: api/ExchangeRate/{currency_id}/{timestamp}
        [Route("api/ExchangeRate/{currency_id}/{timestamp}")]
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutPRCO304_EXCHANGERATE(string currency_id, string timestamp, PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE)
        {
            DateTime timestamp_dt = Convert.ToDateTime(timestamp);

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (currency_id != pRCO304_EXCHANGERATE.currency_id || timestamp_dt != pRCO304_EXCHANGERATE.timestamp)
            {
                return BadRequest();
            }

            db.Entry(pRCO304_EXCHANGERATE).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PRCO304_EXCHANGERATEExists(currency_id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/ExchangeRate
        [ResponseType(typeof(PRCO304_EXCHANGERATE))]
        public async Task<IHttpActionResult> PostPRCO304_EXCHANGERATE(PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.PRCO304_EXCHANGERATE.Add(pRCO304_EXCHANGERATE);

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (PRCO304_EXCHANGERATEExists(pRCO304_EXCHANGERATE.currency_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = pRCO304_EXCHANGERATE.currency_id, pRCO304_EXCHANGERATE.timestamp }, pRCO304_EXCHANGERATE);
        }

        /*        // POST: api/ExchangeRate
                [ResponseType(typeof(PRCO304_EXCHANGERATE))]
                public async void PostPRCO304_EXCHANGERATE(PRCO304_EXCHANGERATE[] rates)
                {
                    for (int i = 1; i < rates.Length; i++)
                    {
                        await PostPRCO304_EXCHANGERATE(rates[i]);
                    }
                }
        */

        // DELETE: api/ExchangeRate/{currency_id}/{timestamp}
        [Route("api/ExchangeRate/{currency_id}/{timestamp}")]
        [ResponseType(typeof(PRCO304_EXCHANGERATE))]
        public async Task<IHttpActionResult> DeletePRCO304_EXCHANGERATE(string currency_id, string timestamp)
        {
            DateTime timestamp_dt = Convert.ToDateTime(timestamp);
            Object[] id = { currency_id, timestamp_dt };

            PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE = await db.PRCO304_EXCHANGERATE.FindAsync(id);
            if (pRCO304_EXCHANGERATE == null)
            {
                return NotFound();
            }

            db.PRCO304_EXCHANGERATE.Remove(pRCO304_EXCHANGERATE);
            await db.SaveChangesAsync();

            return Ok(pRCO304_EXCHANGERATE);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PRCO304_EXCHANGERATEExists(string id)
        {
            return db.PRCO304_EXCHANGERATE.Count(e => e.currency_id == id) > 0;
        }
    }
}