using androidApiLocald.DTO;
using androidApiLocald.Entities;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using Microsoft.EntityFrameworkCore;

namespace androidApiLocald.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class MemberController : ControllerBase
    {
        private readonly androidDbContext androidDBcontext;

        public MemberController(androidDbContext androidDBContext)
        {
            this.androidDBcontext = androidDBContext;
        }

        //로그인 메서드
        [HttpPost("LoginUser")]
        public async Task<ActionResult<MemberDTO>> LoginUser([FromForm] MemberDTO memberDto)
        {
            System.Diagnostics.Trace.WriteLine("Login start");
            //entityFrameworkCore를 이용해 모든 데이터 로드
            using (var context = new androidDbContext())
            {
                var members = context.Members.ToList();
                foreach (var items in members)
                {
                    if (items.Nickname == memberDto.nickname && items.Pw == memberDto.pw)
                    {
                        return Ok();
                        break;
                    }
                }
                return NotFound();
            }
        }

        //모든 유저를 조회
        [HttpGet("GetUsers")]
        public async Task<ActionResult<List<MemberDTO>>> Get()
        {
            var List = await androidDBcontext.Members.Select(
                s => new MemberDTO
                {
                    Id = s.Id,
                    nickname = s.Nickname,
                    pw = s.Pw
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

        //회원가입
        [HttpPost("InsertUser")]
        public async Task<HttpStatusCode> InsertUser([FromForm] MemberDTO memberDto)
        {
            System.Diagnostics.Trace.WriteLine("HttpRquest");
            var entity = new Member()
            {
                Nickname = memberDto.nickname,
                Pw = memberDto.pw
            };
            androidDBcontext.Members.Add(entity);
            await androidDBcontext.SaveChangesAsync();
            return HttpStatusCode.Created;
        }

        //특정 회원만 Id로 추적 삭제
        [HttpDelete("DeleteUser/{Id}")]
        public async Task<HttpStatusCode> DeleteUser(int id)
        {
            var entity = new Member()
            {
                Id = id
            };
            androidDBcontext.Members.Attach(entity);
            androidDBcontext.Members.Remove(entity);
            await androidDBcontext.SaveChangesAsync();
            return HttpStatusCode.OK;
        }
    }
}
