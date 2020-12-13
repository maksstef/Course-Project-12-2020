using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Server.Models;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Server.Controllers
{
    [Route("api/[controller]")]
    public class GetEventsController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        [HttpGet("{id}")]
        public string Get(int id)
        {
            using (CourseProjectContext dbContext = new CourseProjectContext())
            {
                Microsoft.Data.SqlClient.SqlParameter param = new Microsoft.Data.SqlClient.SqlParameter("@id", id);
                var events = dbContext.Events.FromSqlRaw("getevents @id", param).ToList();
                return JsonConvert.SerializeObject(events);
            }
        }


    }
}
