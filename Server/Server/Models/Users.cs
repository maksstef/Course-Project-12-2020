using System;
using System.Collections.Generic;

namespace Server.Models
{
    public partial class Users
    {
        public int UId { get; set; }
        public string Name { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
    }
}
