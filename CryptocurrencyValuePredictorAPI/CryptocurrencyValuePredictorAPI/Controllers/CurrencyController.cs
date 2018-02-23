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
    public class CurrencyController : ApiController
    {
        private CurrencyEntities db = new CurrencyEntities();

        // GET: api/Currency
        public IQueryable<PRCO304_CURRENCY> GetPRCO304_CURRENCY()
        {
            return db.PRCO304_CURRENCY;
        }

        // GET: api/Currency/5
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public IHttpActionResult GetPRCO304_CURRENCY(string id)
        {
            PRCO304_CURRENCY pRCO304_CURRENCY = db.PRCO304_CURRENCY.Find(id);
            if (pRCO304_CURRENCY == null)
            {
                return NotFound();
            }

            return Ok(pRCO304_CURRENCY);
        }

        // PUT: api/Currency/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutPRCO304_CURRENCY(string id, PRCO304_CURRENCY pRCO304_CURRENCY)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != pRCO304_CURRENCY.id)
            {
                return BadRequest();
            }

            db.Entry(pRCO304_CURRENCY).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PRCO304_CURRENCYExists(id))
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

        // POST: api/Currency
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public IHttpActionResult PostPRCO304_CURRENCY(PRCO304_CURRENCY pRCO304_CURRENCY)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.PRCO304_CURRENCY.Add(pRCO304_CURRENCY);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (PRCO304_CURRENCYExists(pRCO304_CURRENCY.id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = pRCO304_CURRENCY.id }, pRCO304_CURRENCY);
        }

        // DELETE: api/Currency/5
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public IHttpActionResult DeletePRCO304_CURRENCY(string id)
        {
            PRCO304_CURRENCY pRCO304_CURRENCY = db.PRCO304_CURRENCY.Find(id);
            if (pRCO304_CURRENCY == null)
            {
                return NotFound();
            }

            db.PRCO304_CURRENCY.Remove(pRCO304_CURRENCY);
            db.SaveChanges();

            return Ok(pRCO304_CURRENCY);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PRCO304_CURRENCYExists(string id)
        {
            return db.PRCO304_CURRENCY.Count(e => e.id == id) > 0;
        }
    }
}