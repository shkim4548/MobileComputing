using System;
using System.Collections.Generic;

namespace androidApiDocker.Entities
{
    public partial class Reply
    {
        public int Id { get; set; }
        public string Writer { get; set; } = null!;
        public string Recontent { get; set; } = null!;
    }
}
