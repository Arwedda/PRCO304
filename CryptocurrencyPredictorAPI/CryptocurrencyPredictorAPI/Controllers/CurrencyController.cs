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
    public class CURRENCYController : ApiController
    {
        private Currencies db = new Currencies();

        // GET: api/PRCO304_CURRENCY
        public IQueryable<PRCO304_CURRENCY> GetPRCO304_CURRENCY()
        {
            return db.PRCO304_CURRENCY;
        }

        // GET: api/PRCO304_CURRENCY/5
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public async Task<IHttpActionResult> GetPRCO304_CURRENCY(string id)
        {
            PRCO304_CURRENCY pRCO304_CURRENCY = await db.PRCO304_CURRENCY.FindAsync(id);
            if (pRCO304_CURRENCY == null)
            {
                return NotFound();
            }

            return Ok(pRCO304_CURRENCY);
        }

        // PUT: api/PRCO304_CURRENCY/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutPRCO304_CURRENCY(string id, PRCO304_CURRENCY pRCO304_CURRENCY)
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
                await db.SaveChangesAsync();
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

        // POST: api/PRCO304_CURRENCY
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public async Task<IHttpActionResult> PostPRCO304_CURRENCY(PRCO304_CURRENCY pRCO304_CURRENCY)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.PRCO304_CURRENCY.Add(pRCO304_CURRENCY);

            try
            {
                await db.SaveChangesAsync();
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

        // DELETE: api/PRCO304_CURRENCY/5
        [ResponseType(typeof(PRCO304_CURRENCY))]
        public async Task<IHttpActionResult> DeletePRCO304_CURRENCY(string id)
        {
            PRCO304_CURRENCY pRCO304_CURRENCY = await db.PRCO304_CURRENCY.FindAsync(id);
            if (pRCO304_CURRENCY == null)
            {
                return NotFound();
            }

            db.PRCO304_CURRENCY.Remove(pRCO304_CURRENCY);
            await db.SaveChangesAsync();

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