import React, { useEffect, useState } from 'react';
// Adjust the import path below if your API utility is in a different location
import { getAllCourses } from '../learnsphere-frontend-with-react/src/api/CourseAPI';

const CourseList = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const data = await getAllCourses();
        // If getAllCourses returns a Response object, use data.json()
        // Otherwise, if it returns data directly, use as is
        if (data && data.json) {
          const json = await data.json();
          setCourses(json);
        } else {
          setCourses(data);
        }
      } catch (err) {
        setError('Failed to fetch courses');
      } finally {
        setLoading(false);
      }
    };
    fetchCourses();
  }, []);

  if (loading) return <div>Loading courses...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2>Course List</h2>
      <ul>
        {courses && courses.length > 0 ? (
          courses.map((course) => (
            <li key={course.id || course.courseId}>{course.name || course.title || JSON.stringify(course)}</li>
          ))
        ) : (
          <li>No courses found.</li>
        )}
      </ul>
    </div>
  );
};

export default CourseList;
