using System.Text.Json.Nodes;

namespace androidApiLocald.DTO
{
    public class BoardJsonDTO
    {
        public int seq { get; set; }
        public DateTime writeDate { get; set; }
        public string? boardContent { get; set; }
        public string? comment { get; set; }
    }
}
