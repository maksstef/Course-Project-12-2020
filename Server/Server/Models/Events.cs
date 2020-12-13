using System;
using System.Collections.Generic;

namespace Server.Models
{
    public partial class Events
    {
        public int EId { get; set; }
        public int? CId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public string Date { get; set; }
    }
}
