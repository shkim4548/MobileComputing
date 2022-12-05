using androidApiLocald.Entities;
using androidApiLocald.DTO;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Serialization;
using Microsoft.EntityFrameworkCore;
using MySql.Data.MySqlClient;
using System.Net;
using System.Security.Cryptography.X509Certificates;
using Newtonsoft.Json.Linq;
using Microsoft.AspNetCore.Hosting.Server;

namespace androidApiLocald.Controllers
{
    public class InsertJson
    {
        public string? seq { get; set; }
        public string? title { get; set; }
        public string? content { get; set; }
    }

    [ApiController]
    [Route("api/[controller]")]
    public class BoardController : ControllerBase
    {
        private readonly androidDbContext androidDBcontext;

        public BoardController(androidDbContext androidDBContext)
        {
            this.androidDBcontext = androidDBContext;
        }

        [HttpPost("GetAll")]
        public async Task<ActionResult<List<BoardJsonDTO>>> GetAll()
        {
            System.Diagnostics.Trace.WriteLine("Get json Called");
            var boardList = await androidDBcontext.BoardJsons.Select(
                s => new BoardJsonDTO
                {
                    boardContent = s.BoardContent
                    //comment = s.Comment
                }
            ).ToListAsync();

            if (boardList.Count < 0)
            {
                System.Diagnostics.Trace.WriteLine("Return NotFound");
                return NotFound();
            }
            else
            {
                //System.Diagnostics.Trace.WriteLine(boardList);
                return boardList;
            }
        }

        //게시글 작성을 위한 원시쿼리
        [HttpPost("InsertJson")]
        public async Task<ActionResult> InsertJson(string title, string content)
        {
            //string seq = $"SELECT COUNT(*) information_schema.columns WHERE table_name = board_json ";
            System.Diagnostics.Trace.WriteLine($"{title}, {content}");
            string sql = $"INSERT INTO board_json(writeDate, boardContent) values(Now(), json_object('title', \'{title}\', 'content', \'{content}\'))";
            System.Diagnostics.Trace.WriteLine(sql);
            using (MySqlConnection conn = new MySqlConnection("server=localhost;port=3306;user=root;password=Blizard5000@;database=androidDb"))
            {
                try
                {
                    conn.Open();
                    MySqlCommand cmd = new MySqlCommand(sql, conn);
                    if (cmd.ExecuteNonQuery() == 1)
                    {
                        System.Diagnostics.Trace.WriteLine("Execute success");
                        System.Diagnostics.Trace.WriteLine(sql);
                        return Ok();
                    }
                    else
                    {
                        System.Diagnostics.Trace.WriteLine("fail to execute");
                        return NotFound();
                    }
                }
                catch(Exception e)
                {
                    System.Diagnostics.Trace.WriteLine(e.ToString());
                    return NotFound();
                }
            }
        }
    }
}
