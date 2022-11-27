using Microsoft.AspNetCore.Mvc;
using AndroidDb.Entities;
using AndroidDb.DTO;
using System.Net;
using Microsoft.EntityFrameworkCore;

namespace AndroidDb.Controllers
{

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
        public async Task<ActionResult<List<BoardDTO>>> Get()
        {
            var List = await androidDBContext.Boards.Select(
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
            if (List.Count < 0)
            {
                return NotFound();
            }
            else
            {
                return List;
            }
        }

        //특정 게시글을 작성
        [HttpPost("InsertBoard")]
        public async Task<HttpStatusCode> InstertBoard(BoardDTO boardDto)
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
