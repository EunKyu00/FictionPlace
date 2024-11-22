import React, { useState } from "react"
import "../CSS/CompanyUserSignup.css"
import signuplogo from "../assets/logoimage2.gif"

function CompanyUserSignup() {
  const [form, setForm] = useState({
    companyName: "",
    businessRegistrationNumber: "",
    email: "",
    password: "",
    password2: "",
    contactPerson: "",
  })

  const [errors, setErrors] = useState({})

  const handleChange = (e) => {
    const { id, value } = e.target
    setForm({
      ...form,
      [id]: value,
    })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    // Validation and form submission logic here
  }

  return (
    <div className="container">
      <img src={signuplogo} alt="signuplogo" className="signup-logo-image" />

      <h2>기업 회원가입</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="companyName">회사 이름</label>
          <input
            className="signup-input"
            type="text"
            id="companyName"
            value={form.companyName}
            onChange={handleChange}
            placeholder="회사 이름을 입력하세요"
          />
          {errors.companyName && <div className="error">{errors.companyName}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="businessRegistrationNumber">사업자 등록번호</label>
          <input
            className="signup-input"
            type="text"
            id="businessRegistrationNumber"
            value={form.businessRegistrationNumber}
            onChange={handleChange}
            placeholder="사업자 등록번호를 입력하세요"
          />
          {errors.businessRegistrationNumber && <div className="error">{errors.businessRegistrationNumber}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="email">담당자 이메일</label>
          <input
            className="signup-input"
            type="email"
            id="email"
            value={form.email}
            onChange={handleChange}
            placeholder="담당자 이메일을 입력하세요"
          />
          {errors.email && <div className="error">{errors.email}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="password">비밀번호</label>
          <input
            className="signup-input"
            type="password"
            id="password"
            value={form.password}
            onChange={handleChange}
            placeholder="비밀번호를 입력하세요"
          />
          {errors.password && <div className="error">{errors.password}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="password2">비밀번호 확인</label>
          <input
            className="signup-input"
            type="password"
            id="password2"
            value={form.password2}
            onChange={handleChange}
            placeholder="비밀번호를 확인하세요"
          />
          {errors.password2 && <div className="error">{errors.password2}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="contactPerson">담당자 이름</label>
          <input
            className="signup-input"
            type="text"
            id="contactPerson"
            value={form.contactPerson}
            onChange={handleChange}
            placeholder="담당자 이름을 입력하세요"
          />
          {errors.contactPerson && <div className="error">{errors.contactPerson}</div>}
        </div>

        <button type="submit" className="submit-btn">
          회원가입
        </button>
      </form>
    </div>
  )
}

export default CompanyUserSignup
