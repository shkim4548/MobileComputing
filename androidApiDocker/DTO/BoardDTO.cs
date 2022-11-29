namespace androidApiDocker.DTO
{
    public class BoardDTO
    {
        public int Id { get; set; }
        public string Title { get; set; } = null!;
        public string Writer { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime StartDate { get; set; }
        public DateTime FinDate { get; set; }
    }
}
