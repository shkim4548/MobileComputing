using System;
using System.Collections.Generic;

namespace AndroidDb.Entities
{
    public partial class Member
    {
        public int Id { get; set; }
        public string Nickname { get; set; } = null!;
        public string Pw { get; set; } = null!;
    }
}
