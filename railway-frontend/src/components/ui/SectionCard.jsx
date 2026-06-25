import PropTypes from 'prop-types'

function SectionCard({ title, children }) {
  return (
    <section className="section-card">
      <h2 className="section-card__title">{title}</h2>
      {children}
    </section>
  )
}

SectionCard.propTypes = {
  title: PropTypes.string.isRequired,
  children: PropTypes.node.isRequired,
}

export default SectionCard
