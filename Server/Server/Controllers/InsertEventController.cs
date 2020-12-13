using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Server.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Server.Controllers
{
    [Route("api/[controller]")]
    public class InsertEventController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        // POST api/<controller>
        [HttpPost]
        public void Post([FromBody]Events value)
        {
            Events events = new Events();
            events.CId = value.CId;
            events.Title = value.Title;
            events.Description = value.Description;
            events.Date = value.Date;

            try
            {
                dbContext.Add(events);
                dbContext.SaveChanges();
            }
            catch (Exception e)
            {

            }
        }

        // PUT api/<controller>/5
        //[HttpPut("{id}")]
        public string Put([FromBody]Events value)
        {
            Events events = dbContext.Events.Where(u => u.EId.Equals(value.EId)).First();
            //events.EId = value.EId;
            //events.CId = value.CId;
            events.Title = value.Title;
            events.Description = value.Description;
            events.Date = value.Date;
            try
            {
                dbContext.Update(events);
                dbContext.SaveChanges();
                return JsonConvert.SerializeObject("User has been registered!");
            }
            catch (Exception ex)
            {
                return JsonConvert.SerializeObject(ex.Message);
            }
        }

        // DELETE api/<controller>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            try
            {
                Events events = dbContext.Events.FirstOrDefault(u => u.EId.Equals(id));
                dbContext.Events.Remove(events);
                dbContext.SaveChangesAsync();
            }
            catch (Exception e)
            {

            }
        }
    }
}
