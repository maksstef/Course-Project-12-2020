using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using Server.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace Server.Controllers
{
    [Route("api/[controller]")]
    public class SynchronizeController : Controller
    {
        // GET api/<controller>/5
        public string Get( )
        {
            using (CourseProjectContext dbContext = new CourseProjectContext())
            {
                return JsonConvert.SerializeObject(dbContext.Members.Count());
            }
        }
    }
}
