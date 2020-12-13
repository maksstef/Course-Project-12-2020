using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Server.Models;
using System;
using System.Collections.Generic;
using System.Collections;
using System.Data.Common;
using System.Data;
using System.IO;
using System.Data.SqlClient;
using Microsoft.EntityFrameworkCore;


// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Server.Controllers
{

    [Route("api/[controller]")]
    public class GetSignUpEventsController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        [HttpGet("{id}")]
        public string Get(int id)
        {

            using (CourseProjectContext dbContext = new CourseProjectContext())
            {
                Microsoft.Data.SqlClient.SqlParameter param = new Microsoft.Data.SqlClient.SqlParameter("@id", id);
                var events = dbContext.Events.FromSqlRaw("getsignupevents @id", param).ToList();
                return JsonConvert.SerializeObject(events);
            }

            //return JsonConvert.SerializeObject(dbContext.Set<Users>().Where(b => b.Login.Contains(value)));
        }

    }
}
