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
    public class SelectAllController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        // GET: api/<controller>
        [HttpGet]
        public string Get()
        {
            //return JsonConvert.SerializeObject(dbContext.Set<Users>().Where(b => b.Login.Contains("maxim")));
            return JsonConvert.SerializeObject(dbContext.Set<Users>());
        }

    }
}
