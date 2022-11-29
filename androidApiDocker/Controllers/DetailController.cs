using androidApiDocker.DTO;
using androidApiDocker.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Net;

namespace androidApiDocker.Controllers
{
    public class DetailController : ControllerBase
    {
        private readonly androidDbContext androidDBContext;

        public DetailController(androidDbContext androidDBContext)
        {
            this.androidDBContext = androidDBContext;
        }

        //게시글 특정 내용 조회
        [HttpGet("GetDetail")]
        public async Task<ActionResult<BoardDTO>> Get(int id)
        {
            BoardDTO content = await androidDBContext.Boards.Select(s => new BoardDTO
            {
                Id = s.Id,
                Content = s.Content,
                Title = s.Title,
                Writer = s.Writer,

            }).FirstOrDefaultAsync(s => s.Id == id);
            if (content == null)
            {
                return NotFound();
            }
            else
            {
                return content;
            }
        }

        //게시글 모든 댓글 조회
        [HttpPost("GetComment")]
        public async Task<ActionResult<List<ReplyDTO>>> GetComment()
        {
            var List = await androidDBContext.Replies.Select(
                s => new ReplyDTO
                {
                    Id = s.Id,
                    Writer = s.Writer,
                    Recontent = s.Recontent
                }
            ).ToListAsync();

            if (List.Count < 0)
            {
                return NotFound();
            }
            else
            {
                return List;
            }
        }

        //댓글 작성
        [HttpPost("InsertReply")]
        public async Task<HttpStatusCode> InsertComment(ReplyDTO replyDto)
        {
            var entity = new Reply()
            {
                Writer = replyDto.Writer,
                Recontent = replyDto.Recontent
            };
            androidDBContext.Replies.Add(entity);
            await androidDBContext.SaveChangesAsync();
            return HttpStatusCode.Created;
        }
    }
}
