using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace androidApiLocald.Entities
{
    public partial class androidDbContext : DbContext
    {
        public androidDbContext()
        {
        }

        public androidDbContext(DbContextOptions<androidDbContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Board> Boards { get; set; } = null!;
        public virtual DbSet<BoardJson> BoardJsons { get; set; } = null!;
        public virtual DbSet<File> Files { get; set; } = null!;
        public virtual DbSet<Member> Members { get; set; } = null!;
        public virtual DbSet<Reply> Replies { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
//#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
                optionsBuilder.UseMySql("server=localhost;port=3306;user=root;password=Blizard5000@;database=androidDb", Microsoft.EntityFrameworkCore.ServerVersion.Parse("8.0.29-mysql"));
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.UseCollation("utf8mb4_0900_ai_ci")
                .HasCharSet("utf8mb4");

            modelBuilder.Entity<Board>(entity =>
            {
                entity.ToTable("board");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Content)
                    .HasColumnType("text")
                    .HasColumnName("content");

                entity.Property(e => e.FinDate)
                    .HasColumnType("datetime")
                    .HasColumnName("finDate");

                entity.Property(e => e.StartDate)
                    .HasColumnType("datetime")
                    .HasColumnName("startDate");

                entity.Property(e => e.Title)
                    .HasMaxLength(30)
                    .HasColumnName("title");

                entity.Property(e => e.Writer)
                    .HasMaxLength(30)
                    .HasColumnName("writer");
            });

            modelBuilder.Entity<BoardJson>(entity =>
            {
                entity.HasNoKey();

                entity.ToTable("board_json");

                entity.Property(e => e.BoardContent)
                    .HasColumnType("json")
                    .HasColumnName("boardContent");

                entity.Property(e => e.Comment)
                    .HasColumnType("json")
                    .HasColumnName("comment");

                entity.Property(e => e.Seq).HasColumnName("seq");

                entity.Property(e => e.WriteDate)
                    .HasColumnType("datetime")
                    .HasColumnName("writeDate");
            });

            modelBuilder.Entity<File>(entity =>
            {
                entity.ToTable("file");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.BoardId).HasColumnName("boardId");

                entity.Property(e => e.Filename)
                    .HasMaxLength(50)
                    .HasColumnName("filename");
            });

            modelBuilder.Entity<Member>(entity =>
            {
                entity.ToTable("member");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Nickname)
                    .HasMaxLength(30)
                    .HasColumnName("nickname");

                entity.Property(e => e.Pw)
                    .HasMaxLength(50)
                    .HasColumnName("pw");
            });

            modelBuilder.Entity<Reply>(entity =>
            {
                entity.ToTable("reply");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Recontent)
                    .HasColumnType("text")
                    .HasColumnName("recontent");

                entity.Property(e => e.Writer)
                    .HasMaxLength(30)
                    .HasColumnName("writer");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
