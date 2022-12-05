using androidApiLocald.Entities;
using androidApiLocald.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace androidApiLocald.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class DetailController : ControllerBase
    {
        private readonly androidDbContext androidDBcontext;

        public DetailController(androidDbContext androidDBContext)
        {
            this.androidDBcontext = androidDBContext;
        }

        //Detail중 게시글의 내용을 불러오는 부분
        [HttpPost("GetDetail")]
        public async Task<ActionResult<List<BoardJsonDTO>>> GetDetail()
        {
            System.Diagnostics.Trace.WriteLine("GetDetail has been called");
            var boardDetail = await androidDBcontext.BoardJsons.Select(
                s => new BoardJsonDTO
                {
                    boardContent = s.BoardContent,
                    comment = s.Comment
                }
            ).ToListAsync();

            if (boardDetail.Count < 0)
            {
                System.Diagnostics.Trace.WriteLine("Return NotFound");
                return NotFound();
            }
            else
            {
                System.Diagnostics.Trace.WriteLine(boardDetail);
                return boardDetail;
            }
        }
    }
}
