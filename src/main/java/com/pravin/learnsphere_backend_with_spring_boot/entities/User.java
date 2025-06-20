package com.pravin.learnsphere_backend_with_spring_boot.entities;

/**
 * Entity representing a user in the job portal system.
 */
// @Getter
// @Setter
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
// @Entity
// @Table(name = "users", indexes = {
//     @Index(name = "idx_user_email", columnList = "email"),
//     @Index(name = "idx_user_role", columnList = "role")
// })
// @ToString(exclude = { "appliedJobs", "skills" })
// public final class User {

//   @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;

//   @Email
//   @NotBlank
//   @Size(max = 255)
//   @Column(name = "email", unique = true, nullable = false)
//   private String email;

//   @NotBlank
//   @Size(min = 8, max = 255)
//   @Column(name = "password", nullable = false)
//   private String password;

//   @Enumerated(EnumType.STRING)
//   @Column(name = "role", nullable = false, length = 50)
//   private Role role;

//   @Enumerated(EnumType.STRING)
// @Column(name = "experience_level", nullable = false)
//   private ExperienceLevel experienceLevel;

//   @Enumerated(EnumType.STRING)
// @Column(name = "status", nullable = false)
// private UserStatus status = UserStatus.ACTIVE;

//   @Size(max = 255)
//   private String name;

//   @Size(max = 512)
//   @Column(name = "avatar_url")
//   private String avatarUrl;

//   @Size(max = 255)
//   private String designation;

//   @Column(name = "verified", nullable = false)
//   private Boolean verified = false;

//   @Size(max = 255)
//   private String location;

//   @Size(max = 1024)
//   private String bio;

//   @Size(max = 20)
//   @Column(name = "phone_number")
//   private String phoneNumber;

//   @Size(max = 512)
//   @Column(name = "linkedin_url")
//   private String linkedinUrl;

//   @Size(max = 512)
//   @Column(name = "github_url")
//   private String githubURL;

//   @Size(max = 255)
//   @Column(name = "job_role")
//   private String jobRole;

//   @Column(name = "is_deleted", nullable = false)
//   private Boolean isDeleted = false;

//   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//   private List<ApplyJob> appliedJobs = new ArrayList<>();

//   @ElementCollection(fetch = FetchType.LAZY)
//   @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
//   @Column(name = "skill", length = 100)
//   private List<String> skills = new ArrayList<>();

//   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//   @Column(name = "created_at", updatable = false)
//   private LocalDateTime createdAt;

//   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//   @Column(name = "updated_at")
//   private LocalDateTime updatedAt;

//   @PrePersist
//   public void prePersist() {
//     LocalDateTime now = LocalDateTime.now();
//     this.createdAt = now;
//     this.updatedAt = now;
//   }

//   @PreUpdate
//   public void preUpdate() {
//     this.updatedAt = LocalDateTime.now();
//   }

//   @Override
//   public boolean equals(Object o) {
//     if (this == o)
//       return true;
//     if (!(o instanceof User user))
//       return false;
//     return id != null && id.equals(user.id);
//   }

//   @Override
//   public int hashCode() {
//     return id != null ? id.hashCode() : 0;
//   }
  

