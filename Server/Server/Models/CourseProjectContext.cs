using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace Server.Models
{
    public partial class CourseProjectContext : DbContext
    {
        public CourseProjectContext()
        {
        }

        public CourseProjectContext(DbContextOptions<CourseProjectContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Events> Events { get; set; }
        public virtual DbSet<Members> Members { get; set; }
        public virtual DbSet<Users> Users { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseSqlServer("Server=LXIBY845;Database=CourseProject;user=max;password=12345678;");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Events>(entity =>
            {
                entity.HasKey(e => e.EId)
                    .HasName("PK__Events__3E2ED64AEBF05A85");

                entity.Property(e => e.EId).HasColumnName("e_id");

                entity.Property(e => e.CId).HasColumnName("c_id");

                entity.Property(e => e.Date)
                    .HasColumnName("date")
                    .HasMaxLength(20);

                entity.Property(e => e.Description)
                    .HasColumnName("description")
                    .HasMaxLength(100);

                entity.Property(e => e.Title)
                    .HasColumnName("title")
                    .HasMaxLength(20);
            });

            modelBuilder.Entity<Members>(entity =>
            {
                entity.HasKey(e => new { e.EventId, e.UId })
                    .HasName("PK_Member");

                entity.Property(e => e.EventId).HasColumnName("event_id");

                entity.Property(e => e.UId).HasColumnName("u_id");
            });

            modelBuilder.Entity<Users>(entity =>
            {
                entity.HasKey(e => e.UId)
                    .HasName("PK__Users__B51D3DEAD582F98F");

                entity.Property(e => e.UId).HasColumnName("u_id");

                entity.Property(e => e.Email)
                    .HasColumnName("email")
                    .HasMaxLength(30);

                entity.Property(e => e.Login)
                    .HasColumnName("login")
                    .HasMaxLength(20);

                entity.Property(e => e.Name)
                    .HasColumnName("name")
                    .HasMaxLength(20);

                entity.Property(e => e.Password)
                    .HasColumnName("password")
                    .HasMaxLength(50);

                entity.Property(e => e.Phone)
                    .HasColumnName("phone")
                    .HasMaxLength(20);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
