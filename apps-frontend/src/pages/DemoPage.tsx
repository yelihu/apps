import React from 'react';

const DemoPage: React.FC = () => {
  return (
    <div style={{ padding: '20px' }}>
      <h1>示例页面</h1>
      <p>这是一个通过路由跳转到的新页面。</p>
      <div>
        <h2>随机文本内容</h2>
        <p>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eget felis in magna commodo sagittis. 
          Donec euismod, nisl eget aliquam ultricies, nunc nisl aliquet nunc, eget aliquam nisl nunc sit amet nisl.
          Sed euismod, nisl eget aliquam ultricies, nunc nisl aliquet nunc, eget aliquam nisl nunc sit amet nisl.
        </p>
        <p>
          这是一些中文假文本。这个页面只是为了展示路由跳转功能而创建的。您可以在此页面上添加任何您想要的内容。
          这只是一个简单的示例，展示如何从一个页面跳转到另一个页面。没有任何特殊的样式或功能。
          您可以根据需要自由修改此页面的内容和样式。
        </p>
      </div>
    </div>
  );
};

export default DemoPage; 