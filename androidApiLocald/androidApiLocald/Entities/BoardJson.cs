using System;
using System.Collections.Generic;
using System.Text.Json.Nodes;

namespace androidApiLocald.Entities
{
    public partial class BoardJson
    {
        public int Seq { get; set; }
        public DateTime WriteDate { get; set; }
        public string? BoardContent { get; set; }
        public string? Comment { get; set; }
    }
}
