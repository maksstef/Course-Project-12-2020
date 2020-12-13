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
    public class RegisterController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        // POST api/<controller>
        [HttpPost]
        public string Post([FromBody]Users value)
        {
            if(!dbContext.Users.Any(user => user.Login.Equals(value.Login)))
            {
                Users user = new Users();
                user.Name = value.Name;
                user.Login = value.Login;
                user.Password = value.Password;
                user.Phone = value.Phone;
                user.Email = value.Email;

                try
                {
                    dbContext.Add(user);
                    dbContext.SaveChanges();
                    return JsonConvert.SerializeObject("User has been registered!");
                }
                catch(Exception e)
                {
                    return JsonConvert.SerializeObject(e.Message);
                }
            }
            else
            {
                return JsonConvert.SerializeObject("User is existing!");
            }
        }

    }
}
