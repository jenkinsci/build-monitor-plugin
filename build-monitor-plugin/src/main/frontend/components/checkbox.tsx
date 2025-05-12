export default function Checkbox({
  label,
  id,
  value,
  setValue,
}: {
  label: string;
  id: string;
  value: boolean;
  setValue: (e: boolean) => void;
}) {
  return (
    <div className="jenkins-checkbox">
      <input
        type="checkbox"
        id={id}
        name={id}
        checked={value}
        onChange={(e) => setValue(e.target.checked)}
      />
      <label htmlFor={id}>{label}</label>
    </div>
  );
}
