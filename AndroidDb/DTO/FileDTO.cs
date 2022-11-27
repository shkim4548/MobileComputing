using System;

namespace AndroidDb.DTO
{
    public class FileDTO
    {
        public int Id { get; set; }
        public int BoardId { get; set; }
        public string Filename { get; set; } = null!;
    }
}
