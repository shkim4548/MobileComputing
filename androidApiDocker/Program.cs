var builder = WebApplication.CreateBuilder(args);

//int hostPort = Int16.Parse(builder.Configuration["Host:Port"]);
/*
builder.WebHost.UseKestrel(options =>
{
    options.Listen(System.Net.IPAddress.Any, hostPort);
});
*/
// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseRouting();

app.UseSwagger();
app.UseSwaggerUI();

app.UseEndpoints(endpoints =>
{
    endpoints.MapGet("/", async context =>
    {
        context.Response.Redirect("swagger");
    });
});


//app.UseHttpsRedirection();

//app.UseAuthorization();

app.MapControllers();

app.Run();
