using System;

namespace AndroidDb.DTO
{
    public class ReplyDTO
    {
        public int Id { get; set; }
        public string Writer { get; set; } = null!;
        public string Recontent { get; set; } = null!;
    }
}
