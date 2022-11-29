using androidApiDocker.DTO;
using androidApiDocker.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Net;
using System.Text.Json;

namespace androidApiDocker.Controllers
{
    public class ContentType
    {
        public string? title { get; set; }
        public string? content { get; set; }
        public string? writer { get; set; }
    }

    [ApiController]
    [Route("api/[controller]")]
    public class BoardListController : ControllerBase
    {
        private readonly androidDbContext androidDBContext;

        public BoardListController(androidDbContext androidDBContext)
        {
            this.androidDBContext = androidDBContext;
        }

        //모든 게시판 리스트를 조회
        [HttpGet("GetBoardList")]
        public async Task<ActionResult<string>> Get()
        {
            var list = await androidDBContext.Boards.Select(
                s => new BoardDTO
                {
                    Id = s.Id,
                    Title = s.Title,
                    Writer = s.Writer,
                    //Content = s.Content,
                    StartDate = s.StartDate,
                    FinDate = s.FinDate
                }
            ).ToListAsync();
            //List를 json 형태로 바꾸는 부분
            string strJson = JsonSerializer.Serialize<List<BoardDTO>>(list, new JsonSerializerOptions()
            {
                WriteIndented = true
            });
            
            if (strJson != null)
            {
                return NotFound();
            }
            else
            {
                return strJson;
            }
        }

        //특정 게시글을 작성
        [HttpPost("InsertBoard")]
        public async Task<HttpStatusCode> InstertBoard([FromForm] BoardDTO boardDto)
        {
            var entity = new Board()
            {
                Title = boardDto.Title,
                Writer = boardDto.Writer,
                Content = boardDto.Content,
                StartDate = boardDto.StartDate,
                FinDate = boardDto.FinDate
            };
            androidDBContext.Boards.Add(entity);
            await androidDBContext.SaveChangesAsync();
            return HttpStatusCode.Created;
        }

        //특정 게시글을 삭제
        [HttpDelete("DeleteBoard/{Id}")]
        public async Task<HttpStatusCode> DeleteBoardContent(int id)
        {
            var entity = new Board()
            {
                Id = id
            };
            androidDBContext.Boards.Attach(entity);
            androidDBContext.Boards.Remove(entity);
            await androidDBContext.SaveChangesAsync();
            return HttpStatusCode.OK;
        }
    }
}
