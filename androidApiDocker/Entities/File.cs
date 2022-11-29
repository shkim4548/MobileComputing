using System;
using System.Collections.Generic;

namespace androidApiDocker.Entities
{
    public partial class File
    {
        public int Id { get; set; }
        public int BoardId { get; set; }
        public string Filename { get; set; } = null!;
    }
}
