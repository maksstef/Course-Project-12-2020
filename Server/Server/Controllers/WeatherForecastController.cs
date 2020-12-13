using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Routing;
using Microsoft.Extensions.Logging;
using Server.Models;
//using System.Web;
//using System.Web.Mvc;
//using System.Web.WebPages.Html;
//using System.Web.Routing;

namespace Server.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class WeatherForecastController : Controller
    {
        CourseProjectContext dbContext = new CourseProjectContext();

        private static readonly string[] Summaries = new[]
        {
            "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching"
        };

        private readonly ILogger<WeatherForecastController> _logger;

        public WeatherForecastController(ILogger<WeatherForecastController> logger)
        {
            _logger = logger;
        }

        [System.Web.Mvc.HttpGet]
        public ActionResult Get()
        {

            //return dbContext.Set<Users>().ToList();
            StringBuilder response = new StringBuilder();

            List<Users> users = dbContext.Set<Users>().ToList();

            response.AppendLine("<table class=\"table\"> <thead class=\"thead-dark\"><tr><th scope=\"col\"> Name </th>"+
                               "<th scope = \"col\" > Login </ th >"+
                                "<th scope = \"col\" > Mail </ th >"+
                                "<th scope = \"col\" > Phone </ th >"+
                                "</tr >"+
                                "</thead >"+
                                "< tbody > ");
            foreach (var i in users)
            {
                response.AppendLine("<tr>< th scope = \"row\" >"+ i.Name +"</ th >< td >"+ i.Login +"</ td >< td >"+ i.Email +"</ td >< td >"+ i.Phone +"</ td ></ tr >"); //<tr>< th scope = "row" > 1 </ th >< td > Mark </ td >< td > Otto </ td > < td > @mdo </ td ></ tr >
            }
            response.AppendLine("</tbody></table>");

            ViewData["table"] = response.ToString();

            //Response.BodyWriter(response.ToString())
            //Response.Write(response.ToString());
           
            //ViewBag.Rd = rd;
            return View();
        }

    }
}
