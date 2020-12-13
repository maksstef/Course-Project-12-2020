using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Server.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Server.Controllers
{
    [Route("api/[controller]")]
    public class InsertMemberController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();
        // POST api/<controller>
        [HttpPost]
        public void Post([FromBody]Members value)
        {
            Members member = new Members();
            member.EventId = value.EventId;
            member.UId = value.UId;

            try
            {
                dbContext.Add(member);
                dbContext.SaveChanges();
            }
            catch (Exception e)
            {

            }
        }

        // DELETE api/<controller>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            try
            {
                Microsoft.Data.SqlClient.SqlParameter param = new Microsoft.Data.SqlClient.SqlParameter("@eid", id);
                dbContext.Events.FromSqlRaw("delmembers @eid", param).ToList();
            }
            catch (Exception e)
            {

            }
        }
    }
}
