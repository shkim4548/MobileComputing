﻿using System;
using System.Collections.Generic;

namespace androidApiLocald.Entities
{
    public partial class Member
    {
        public int Id { get; set; }
        public string Nickname { get; set; } = null!;
        public string Pw { get; set; } = null!;
    }
}
