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
    [Route("api/[controller]/{id:int}/{id2:int}")]
    //[Route("api/[controller]")]
    public class DelMemberController : Controller
    {
        public void Delete(int id, int id2)
        {
            using (CourseProjectContext dbContext = new CourseProjectContext())
            {
                try
                {
                    Microsoft.Data.SqlClient.SqlParameter param = new Microsoft.Data.SqlClient.SqlParameter("@eid", id);
                    Microsoft.Data.SqlClient.SqlParameter param2 = new Microsoft.Data.SqlClient.SqlParameter("@uid", id2);
                    dbContext.Events.FromSqlRaw("delmember @eid, @uid", param, param2).ToList();
                }
                catch(Exception e)
                {
                    
                }
            }
        }
    }
}
