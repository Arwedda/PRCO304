using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using CryptocurrencyValuePredictorAPI.Models;

namespace CryptocurrencyValuePredictorAPI.Controllers
{
    public class ExchangeRateController : ApiController
    {
        private ExchangeRateEntities db = new ExchangeRateEntities();

        // GET: api/ExchangeRate
        public IQueryable<PRCO304_EXCHANGERATE> GetPRCO304_EXCHANGERATE()
        {
            return db.PRCO304_EXCHANGERATE;
        }

        // GET: api/ExchangeRate/5
        [ResponseType(typeof(PRCO304_EXCHANGERATE))]
        public IHttpActionResult GetPRCO304_EXCHANGERATE(string id)
        {
            PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE = db.PRCO304_EXCHANGERATE.Find(id);
            if (pRCO304_EXCHANGERATE == null)
            {
                return NotFound();
            }

            return Ok(pRCO304_EXCHANGERATE);
        }

        // PUT: api/ExchangeRate/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutPRCO304_EXCHANGERATE(string id, PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != pRCO304_EXCHANGERATE.currency_id)
            {
                return BadRequest();
            }

            db.Entry(pRCO304_EXCHANGERATE).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PRCO304_EXCHANGERATEExists(id))
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
        public IHttpActionResult PostPRCO304_EXCHANGERATE(PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.PRCO304_EXCHANGERATE.Add(pRCO304_EXCHANGERATE);

            try
            {
                db.SaveChanges();
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

            return CreatedAtRoute("DefaultApi", new { id = pRCO304_EXCHANGERATE.currency_id }, pRCO304_EXCHANGERATE);
        }

        // DELETE: api/ExchangeRate/5
        [ResponseType(typeof(PRCO304_EXCHANGERATE))]
        public IHttpActionResult DeletePRCO304_EXCHANGERATE(string id)
        {
            PRCO304_EXCHANGERATE pRCO304_EXCHANGERATE = db.PRCO304_EXCHANGERATE.Find(id);
            if (pRCO304_EXCHANGERATE == null)
            {
                return NotFound();
            }

            db.PRCO304_EXCHANGERATE.Remove(pRCO304_EXCHANGERATE);
            db.SaveChanges();

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