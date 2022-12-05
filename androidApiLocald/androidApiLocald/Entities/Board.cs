using System;
using System.Collections.Generic;

namespace androidApiLocald.Entities
{
    public partial class Board
    {
        public int Id { get; set; }
        public string Title { get; set; } = null!;
        public string Writer { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime StartDate { get; set; }
        public DateTime FinDate { get; set; }
    }
}
